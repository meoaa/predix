#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
predictor/forecast.py
CLI  : python forecast.py <csvPath> <periods:int> <freq>
ex   : python forecast.py sales.csv 4 W-MON
"""

import sys
import logging
import warnings

# ────────────────────────────────
# 0)  모든 로그·경고 완전 차단
# ────────────────────────────────
logging.disable(logging.CRITICAL)        # DEBUG/INFO/WARN 전부 mute
warnings.filterwarnings("ignore")

# ────────────────────────────────
# 1)  이제 Prophet import
# ────────────────────────────────
from prophet import Prophet              # noqa: E402
import pandas as pd

# ────────────────────────────────
# 2)  인자 파싱
# ────────────────────────────────
if len(sys.argv) != 4:
    sys.stderr.write(
        "USAGE: python forecast.py <csvPath> <periods:int> <freq>\n"
    )
    sys.exit(1)

csv_path = sys.argv[1]
periods  = int(sys.argv[2])
freq     = sys.argv[3]                   # ‘W-MON’, ‘MS’ …

# ────────────────────────────────
# 3)  모델 학습 & 예측
# ────────────────────────────────
df = pd.read_csv(csv_path, names=["ds", "y"])
df["floor"] = 0
cap = df["y"].max()
df["cap"] = cap

model = (
    Prophet(weekly_seasonality=True, growth="logistic")
    if freq.startswith("W")
    else Prophet(growth="logistic")
)
model.fit(df)

future = model.make_future_dataframe(periods=periods, freq=freq)
future["floor"] = 0
future["cap"] = cap
forecast = model.predict(future).tail(periods)[
    ["ds", "yhat", "yhat_lower", "yhat_upper"]
]

# ────────────────────────────────
# 4)  순수 JSON만 stdout 에 출력
# ────────────────────────────────
print(
    forecast.to_json(
        orient="records",
        date_format="iso",   # ISO-8601 (yyyy-MM-ddTHH:mm:ssZ)
        date_unit="s",
    )
)
