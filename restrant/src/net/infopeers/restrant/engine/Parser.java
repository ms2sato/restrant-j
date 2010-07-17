package net.infopeers.restrant.engine;

/**
 * パーサ
 * @author ms2
 *
 */
public interface Parser {

	/**
	 * パースする
	 * @param params パラメータ
	 * @param path URLパス
	 * @return パースできたら真。さもなくば偽。
	 */
	boolean parse(EditableParams params, String path);
}
