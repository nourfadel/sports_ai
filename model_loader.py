from pathlib import Path
import json
import joblib

try:
    import tensorflow as tf
except ImportError:
    tf = None


BASE_DIR = Path(__file__).resolve().parent
MODELS_DIR = BASE_DIR / "models"

KERAS_MODEL_PATH = MODELS_DIR / "model_final_v8_notebook.keras"
SUBJECT_STATS_PATH = MODELS_DIR / "subject_stats.pkl"
MODEL_CONFIG_PATH = MODELS_DIR / "model_config_v8_notebook.json"
UNIFIED_MODEL_PATH = MODELS_DIR / "unified_model.pkl"


class ModelRegistry:
    def __init__(self):
        self.keras_model = None
        self.subject_stats = None
        self.model_config = None
        self.unified_model = None

    def load_all(self):
        self.model_config = self._load_config()
        self.subject_stats = self._load_subject_stats()
        self.keras_model = self._load_keras_model()
        self.unified_model = self._load_unified_model()
        return self

    def _load_config(self):
        if not MODEL_CONFIG_PATH.exists():
            return None

        with open(MODEL_CONFIG_PATH, "r", encoding="utf-8") as file:
            return json.load(file)

    def _load_subject_stats(self):
        if not SUBJECT_STATS_PATH.exists():
            return None

        return joblib.load(SUBJECT_STATS_PATH)

    def _load_keras_model(self):
        if tf is None or not KERAS_MODEL_PATH.exists():
            return None

        return tf.keras.models.load_model(KERAS_MODEL_PATH,compile = False)

    def _load_unified_model(self):
        if not UNIFIED_MODEL_PATH.exists():
            return None

        try:
            return joblib.load(UNIFIED_MODEL_PATH)
        except Exception as ex:
            print(f"[MODEL] Could not load unified model: {ex}")
            return None

    def health(self):
        return {
            "kerasModelLoaded": self.keras_model is not None,
            "subjectStatsLoaded": self.subject_stats is not None,
            "modelConfigLoaded": self.model_config is not None,
            "unifiedModelLoaded": self.unified_model is not None,
            "modelVersion": self.model_config.get("model_version") if self.model_config else None,
            "windowLength": self.model_config.get("window_len") if self.model_config else None,
            "samplingRate": self.model_config.get("fs") if self.model_config else None,
            "signals": self.model_config.get("signals") if self.model_config else None,
            "threshold": self.model_config.get("threshold") if self.model_config else None,
        }


model_registry = ModelRegistry()