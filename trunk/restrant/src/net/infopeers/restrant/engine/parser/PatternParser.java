package net.infopeers.restrant.engine.parser;

import net.infopeers.restrant.engine.params.EditableParams;

/**
 * パーサ
 * @author ms2
 *
 */
public interface PatternParser {

	/**
	 * パースする
	 * @param params パラメータ
	 * @param path URLパターン
	 * @return パースできたら真。さもなくば偽。
	 */
	boolean parse(EditableParams params, String path);
	
	
	/**
	 * praceHolderの値が固定で存在すればその値を返す
	 * @return
	 */
	String findSpecifiedPlaceHolder(String placeHolder);
}
