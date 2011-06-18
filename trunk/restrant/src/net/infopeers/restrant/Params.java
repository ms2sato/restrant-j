package net.infopeers.restrant;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Set;

/**
 * リクエストに含まれる各種パラメータ
 * 
 * @author ms2
 *
 */
public interface Params {
	
	/**
	 * HTTPメソッドを取得する
	 */
	String getMethod();

	/**
	 * Content-Type
	 */
	String getContentType();
	
	/**
	 * インプットストリームで取得する
	 * @throws IOException 
	 */
	InputStream getInputStream() throws IOException;
	
	/**
	 * パラメータとして取得できず、
	 * getInputStreamでのみデータが受信できるかどうか
	 */
	boolean isStreamContent();
	
	/**
	 * 全てのパラメータの名前セットを取得する
	 */
	Set<String> nameSet();

	/**
	 * リクエストのパラメータを取得する
	 * @param key キー
	 * @return リクエストのパラメータ。存在しない場合はnull。
	 */
	String getParameter(String key);

	/**
	 * リクエストのパラメータを複数取得する
	 * @param key キー
	 * @return リクエストのパラメータの配列
	 */
	String[] getParameters(String key);

	/**
	 * リクエストパラメータの名前を取得する
	 * @return リクエストパラメータの名前
	 */
	Enumeration<String> getParameterNames();
	
	
	/**
	 * URLパスから取得した拡張パラメータを取得する
	 * @param key キー文字列
	 * @return 拡張パラメータ
	 */
	String getExtension(String key);

	/**
	 * URLパスから取得した拡張パラメータを複数取得する
	 * @param key キー文字列
	 * @return 拡張パラメータの配列
	 */
	String[] getExtensions(String key);
	
	
	/**
	 * URLパスから取得した拡張パラメータの名前を取得する
	 * @return 拡張パラメータの名前のSet
	 */
	Set<String> getExtensionNames();

	/**
	 * 全てのパラメータから合致するものを取得する。
	 * 以下の順序で取得する。
	 * <ol>
	 * <li>URLパス内変数
	 * <li>リクエストパラメータ
	 * </ol>
	 * <p>
	 * リクエストパラメータによって処理を誤動作させないために
	 * この順序の動作をする。
	 * <p>
	 * @param key キー文字列
	 * @return URLパスを優先して探す。リクエストパラメータにも存在しなければnull。
	 */
	String get(String key);
	
	
	/**
	 * 全てのパラメータから合致するものを複数取得する。
	 * 以下の順序で取得する。
	 * <ol>
	 * <li>URLパス内変数
	 * <li>リクエストパラメータ
	 * </ol>
	 * <p>
	 * リクエストパラメータによって処理を誤動作させないために
	 * この順序の動作をする。
	 * <p>
	 * @param key キー文字列
	 * @return URLパスを優先して探す。リクエストパラメータにも存在しなければnull。
	 */
	String[] gets(String key);

	/**
	 * keyに対応する文字列をclsとしてパースしたオブジェクトで取得する
	 * @param cls パースする型
	 * @param key キー文字列
	 * @return オブジェクト
	 */
	Object asObject(Class<?> cls, String key);

	/**
	 * keyに対応する文字列をbooleanとしてパースして取得する
	 * @param key キー文字列
	 * @return boolean
	 */
	boolean asBoolean(String key);

	/**
	 * keyに対応する文字列をintとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	int asInt(String key);

	/**
	 * keyに対応する文字列をlongとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	long asLong(String key);

	/**
	 * keyに対応する文字列をdoubleとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	double asDouble(String key);

	/**
	 * keyに対応する文字列をBooleanとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	Boolean asBooleanObject(String key);

	/**
	 * keyに対応する文字列をIntegerとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	Integer asInteger(String key);

	/**
	 * keyに対応する文字列をLongとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	Long asLongObject(String key);

	/**
	 * keyに対応する文字列をDoubleとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	Double asDoubleObject(String key);

	/**
	 * keyに対応する文字列をBigIntegerとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	BigInteger asBigInteger(String key);

	/**
	 * keyに対応する文字列をBigDecimalとしてパースして取得する
	 * @param key キー文字列
	 * @return パースした値
	 */
	BigDecimal asBigDecimal(String key);

}