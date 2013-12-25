package net.kirauks.andwake.utils;

import android.widget.EditText;

public final class FormValidator {
	public static boolean isEmpty(EditText edit) {
		return edit.getText().toString().equals("");
	}

	public static boolean isHexadecimal(EditText edit) {
		return edit.getText().toString().matches("[0-9a-fA-F]*");
	}

	public static boolean isLength(EditText edit, int length) {
		return edit.getText().toString().length() == length;
	}

	public static boolean isLengthMax(EditText edit, int length) {
		return edit.getText().toString().length() > length;
	}

	public static boolean isLengthMin(EditText edit, int length) {
		return edit.getText().toString().length() < length;
	}
}
