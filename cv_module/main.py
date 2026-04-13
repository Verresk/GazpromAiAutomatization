import cv2
import re

import cv_module.settings as settings


def preprocess_roi(roi):
    """Предобработка изображения: апскейл, ЧБ, шумоподавление"""
    gray = cv2.cvtColor(roi, cv2.COLOR_BGR2GRAY)
    enlarged = cv2.resize(gray, None, fx=2, fy=2, interpolation=cv2.INTER_CUBIC)
    _, binary = cv2.threshold(enlarged, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    return binary


def normalize_numeric_text(text: str) -> str:
    """Нормализация текста"""
    text = text.strip()
    text = text.replace(",", ".")
    text = text.replace ("O", "0")
    text = re.sub(r"[^0-9.\-+]", "", text)
    return text



