package net.infopeers.restrant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 公開メソッドを示すアノテーション
 * 
 * @author ms2
 *
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Method {

	/**
	 * 公開メソッドとしての名前を取得する
	 */
	String name() default "";
	
	/**
	 * プレースホルダを置き換える引数名の配列を取得する
	 */
	String[] args() default {};
	
	/**
	 * プレースホルダを置き換える引数名の配列を取得する
	 * （短縮表記用）
	 */
	String[] value() default {};
}
