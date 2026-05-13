from pydantic import BaseModel
from typing import List, Optional, Dict


class SensorWindowRequest(BaseModel):
    sessionId: int
    playerProfileId: int

    heartRates: List[float]
    hrvs: List[float]
    speeds: List[float]
    distances: List[float]
    playerLoads: List[float]
    sprintCounts: List[int]
    impactForces: Optional[List[float]] = []
    bodyTemperatures: List[float]
    respiratoryRates: List[float]


class RiskAnalysisResponse(BaseModel):
    sessionId: int
    playerProfileId: int

    riskLevel: str
    fatigueLevel: str
    finalScore: float
    mfi: float
    mlProbability: Optional[float] = None
    recommendation: str

    components: Dict[str, float]