/**
 * 
 */
package jp.co.scio.syare.util.pool;

public interface Creator<Key, Value> {
	Value create(Key key);
}