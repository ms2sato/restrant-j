package net.infopeers.restrant.commons.http.response;

public enum FailerCauses {
	RETRYABLE, // ���g���C�\
	MAINTENANCE, // GAE�̃����e��
	ILLEGALINPUT, // �s���ȓ���
	ILLEGALDATASTATE, // �s���ȃf�[�^�̏��
	OTHERS; // �s���ȃG���[
}