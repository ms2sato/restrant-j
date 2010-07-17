package net.infopeers.restrant.engine;

/**
 * プレースホルダーのフォーマッター
 * @author ms2
 *
 */
public interface PlaceholderFormatter {

	/**
	 * プレースホルダーのフォーマットに従っているか判断する
	 * @param target 対象
	 * @return 従っていたら真。さもなくば偽。
	 */
	boolean isPlaceholder(String target);

	/**
	 * プレースホルダー化する
	 * @param label ホルダーのラベル
	 * @return プレースホルダーのフォーマットに従った文字列
	 */
	String enPlaceholder(String label);

	/**
	 * プレースホルダー文字列からラベルを取得する
	 * @param placeholder プレースホルダーのフォーマットに従っている文字列
	 * @return ラベル
	 */
	String dePlaceholder(String placeholder);

}