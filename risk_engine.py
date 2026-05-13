import numpy as np


def _safe_mean(values, default=0.0):
    values = [v for v in values if v is not None]
    if not values:
        return default
    return float(np.mean(values))


def _safe_max(values, default=0.0):
    values = [v for v in values if v is not None]
    if not values:
        return default
    return float(np.max(values))


def _clip(value, min_value=0.0, max_value=1.0):
    return float(np.clip(value, min_value, max_value))


def compute_mfi(data):
    hr_mean = _safe_mean(data.heartRates)
    hrv_mean = _safe_mean(data.hrvs)
    speed_mean = _safe_mean(data.speeds)
    player_load_max = _safe_max(data.playerLoads)
    sprint_count_max = max(data.sprintCounts) if data.sprintCounts else 0
    impact_max = _safe_max(data.impactForces)
    temp_mean = _safe_mean(data.bodyTemperatures)
    resp_mean = _safe_mean(data.respiratoryRates)

    hr_score = _clip((hr_mean - 60) / 120)
    hrv_score = _clip(1 - (hrv_mean / 100))
    speed_score = _clip(speed_mean / 9)
    load_score = _clip(player_load_max / 1000)
    sprint_score = _clip(sprint_count_max / 30)
    impact_score = _clip(impact_max / 500)
    temp_score = _clip((temp_mean - 36.5) / 3)
    resp_score = _clip((resp_mean - 12) / 28)

    mfi = (
        hr_score * 0.25 +
        hrv_score * 0.25 +
        speed_score * 0.10 +
        load_score * 0.15 +
        sprint_score * 0.05 +
        impact_score * 0.05 +
        temp_score * 0.10 +
        resp_score * 0.05
    )

    components = {
        "hrScore": round(hr_score, 3),
        "hrvScore": round(hrv_score, 3),
        "speedScore": round(speed_score, 3),
        "loadScore": round(load_score, 3),
        "sprintScore": round(sprint_score, 3),
        "impactScore": round(impact_score, 3),
        "temperatureScore": round(temp_score, 3),
        "respiratoryScore": round(resp_score, 3),
    }

    return round(float(mfi), 3), components


def analyze_risk(data, ml_probability=None):
    mfi, components = compute_mfi(data)

    if ml_probability is None:
        final_score = mfi
    else:
        final_score = 0.6 * ml_probability + 0.4 * mfi

    if final_score >= 0.65:
        risk_level = "HIGH_RISK"
        fatigue_level = "HIGH"
        recommendation = "Replace player immediately and allow recovery."
    elif final_score >= 0.45:
        risk_level = "MODERATE_RISK"
        fatigue_level = "MEDIUM"
        recommendation = "Monitor closely and reduce player load."
    else:
        risk_level = "LOW_RISK"
        fatigue_level = "LOW"
        recommendation = "Player condition is stable. Continue monitoring."

    return {
        "riskLevel": risk_level,
        "fatigueLevel": fatigue_level,
        "finalScore": round(float(final_score), 3),
        "mfi": mfi,
        "mlProbability": ml_probability,
        "recommendation": recommendation,
        "components": components,
    }