package net.infopeers.restrant.engine;

/**
 * InvokerBuilderのライフサイクル。
 * リクエストごとに設定を読み直す（デバッグ用）か、
 * サーバ起動時に決定するものか（本番用）
 * @author ms2
 *
 */
public interface InvokerBuilderLifecycle {

	/**
	 * サーバー起動時
	 */
	void onInit();
	
	/**
	 * リクエスト受信時
	 */
	void onRequest();
	
	
	InvokerBuilder getInvokerBuilder();
}
