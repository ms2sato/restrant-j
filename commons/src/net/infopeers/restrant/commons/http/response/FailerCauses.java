package net.infopeers.restrant.commons.http.response;

public enum FailerCauses {
	RETRYABLE, // リトライ可能
	MAINTENANCE, // GAEのメンテ中
	ILLEGALINPUT, // 不正な入力
	ILLEGALDATASTATE, // 不正なデータの状態
	OTHERS; // 不明なエラー
}