package net.infopeers.restrant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リクエスト時に設定されるフィールドを示す
 * @author ms2
 *
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Request {
	
	/**
	 * パラメータ名を取得する
	 * @return パラメータ名
	 */
	String name() default "";
	
}
