package br.com.mybaby;

public class VisibilidadeManager {
	private static boolean isMainActivityVisible;

	public static boolean isMainActivityVisible() {
		return isMainActivityVisible;
	}

	public static void setMainActivityVisible(boolean isMainActivityVisible) {
		VisibilidadeManager.isMainActivityVisible = isMainActivityVisible;
	}
}
