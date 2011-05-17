package net.infopeers.restrant.engine.parser;

import net.infopeers.restrant.engine.params.EditableParams;

/**
 * パーサ
 * @author ms2
 *
 */
public interface UrlParser {

	/**
	 * パースする
	 * @param params パラメータ
	 * @param path URLパス
	 * @return パースできたら真。さもなくば偽。
	 */
	boolean parse(EditableParams params, String path);
}