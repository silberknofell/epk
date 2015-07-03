package de.geihe.epk_orm.pojo;

public class Empfehlung {

	public static final int EMPF_UNBEKANNT = 0;
	public static final int EMPF_GYM = 1;
	public static final int EMPF_EINGESCHR = 2;
	public static final int EMPF_REAL = 3;

	private final static String[] empfstring = { "0", "Gy", "E", "Realschule" };

	public static String toString(int empf) {
		if (empf < 1) {
			return null;
		}
		if (empf >= empfstring.length) {
			return null;
		}
		return empfstring[empf];
	}

	public static boolean isGym(int empf) {
		return (empf == 1);
	}

	public static int schildStrToEmpf(String schildStr) {
		if (schildStr.equals("Gy")) {
			return EMPF_GYM;
		}
		if (schildStr.equals("R/Gy")) {
			return EMPF_EINGESCHR;
		}
		if (schildStr.equals("R")) {
			return EMPF_REAL;
		}
		return EMPF_UNBEKANNT;
	}

}
