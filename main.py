from fastapi import FastAPI
from schemas import SensorWindowRequest, RiskAnalysisResponse
from risk_engine import analyze_risk
from model_loader import model_registry

app = FastAPI(
    title="Adaii ML Service",
    version="1.0.0"
)


@app.on_event("startup")
def startup_event():
    model_registry.load_all()


@app.get("/health")
def health():
    return {
        "status": "UP",
        "service": "adaii-ml-service"
    }


@app.get("/health/model")
def model_health():
    return model_registry.health()


@app.post("/api/ai/analyze-session", response_model=RiskAnalysisResponse)
def analyze_session(request: SensorWindowRequest):
    result = analyze_risk(request, ml_probability=None)

    return RiskAnalysisResponse(
        sessionId=request.sessionId,
        playerProfileId=request.playerProfileId,
        riskLevel=result["riskLevel"],
        fatigueLevel=result["fatigueLevel"],
        finalScore=result["finalScore"],
        mfi=result["mfi"],
        mlProbability=result["mlProbability"],
        recommendation=result["recommendation"],
        components=result["components"]
    )