package com.example.quiz11.constants;

public enum QuesType {
	SINGLE("single"), //
	MULTI("multi"), //
	TEXT("text");

	private String type;

	private QuesType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static boolean checkType(String type) {
//		if (type.equalsIgnoreCase(QuesType.SINGLE.toString())//
//				|| type.equalsIgnoreCase(QuesType.MULTI.toString()) //
//				|| type.equalsIgnoreCase(QuesType.TEXT.toString())
//
//		) {
//			return true;
//		}

		// 等同上面那一段(使用迴圈替換
		// QuesType.values() 可以取得在 QuesType 此 enum 中所有的 type
		for (QuesType item : QuesType.values()) {
			if (item.getType().equalsIgnoreCase(type)) {
				return true;
			}
		}

		return false;
	}
}
