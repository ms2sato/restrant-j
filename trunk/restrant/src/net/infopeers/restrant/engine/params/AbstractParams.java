package net.infopeers.restrant.engine.params;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import net.infopeers.util.Convertor;

/**
 * Paramsの共通実装。Extensionはもともとのパラメータを上書きする概念。
 * 
 * @author ms2
 * 
 */
public abstract class AbstractParams implements EditableParams {

	private final ExtensionMultimap exMultimap;

	protected List<String> getExtensionListOf(String key) {
		return exMultimap.getExtensionListOf(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getExtensionNames()
	 */
	public Set<String> getExtensionNames() {
		return exMultimap.getExtensionNames();
	}

	/**
	 * 
	 */
	public AbstractParams(ExtensionMultimap exMultimap) {
		super();
		this.exMultimap = exMultimap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getExtension(java.lang.String)
	 */
	public String getExtension(String key) {
		List<String> list = getExtensionListOf(key);
		if (list == null)
			return null;
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getExtensions(java.lang.String)
	 */
	public String[] getExtensions(String key) {
		List<String> list = getExtensionListOf(key);
		if (list == null)
			return new String[] {};
		return list.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#get(java.lang.String)
	 */
	public String get(String key) {
		String value = getExtension(key);
		if (value == null)
			return getParameter(key);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#gets(java.lang.String)
	 */
	public String[] gets(String key) {
		String[] values = getExtensions(key);
		if (values == null)
			return getParameters(key);
		if (values.length == 0)
			return getParameters(key);
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asObject(java.lang.Class,
	 * java.lang.String)
	 */
	public Object asObject(Class<?> cls, String key) {

		if (cls.isArray()) {
			if (!nameSet().contains(key)) {
				throw new IllegalStateException(key + "に一致する値がありません");
			}

			String[] values = gets(key);
			Object res = Array.newInstance(cls.getComponentType(),
					values.length);
			for (int i = 0; i < values.length; ++i) {
				Array.set(res, i,
						Convertor.convert(values[i], cls.getComponentType()));
			}
			return res;
		}

		String value = get(key);
		if (value == null)
			return null;
		return Convertor.convert(value, cls);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asBoolean(java.lang.String)
	 */
	public boolean asBoolean(String key) {
		String value = get(key);
		if (value == null)
			return false;
		return Boolean.parseBoolean(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asInt(java.lang.String)
	 */
	public int asInt(String key) {
		String value = get(key);
		if (value == null)
			return 0;
		return Integer.parseInt(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asLong(java.lang.String)
	 */
	public long asLong(String key) {
		String value = get(key);
		if (value == null)
			return 0;
		return Long.parseLong(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asDouble(java.lang.String)
	 */
	public double asDouble(String key) {
		String value = get(key);
		if (value == null)
			return 0;
		return Double.parseDouble(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asBooleanObject(java.lang.String)
	 */
	public Boolean asBooleanObject(String key) {
		String value = get(key);
		if (value == null)
			return false;
		return Boolean.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asInteger(java.lang.String)
	 */
	public Integer asInteger(String key) {
		String value = get(key);
		if (value == null)
			return Integer.valueOf(0);
		return Integer.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asLongObject(java.lang.String)
	 */
	public Long asLongObject(String key) {
		String value = get(key);
		if (value == null)
			return Long.valueOf(0);
		return Long.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asDoubleObject(java.lang.String)
	 */
	public Double asDoubleObject(String key) {
		String value = get(key);
		if (value == null)
			return Double.valueOf(0.0);
		return Double.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asBigInteger(java.lang.String)
	 */
	public BigInteger asBigInteger(String key) {
		String value = get(key);
		if (value == null)
			return BigInteger.ZERO;
		return new BigInteger(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#asBigDecimal(java.lang.String)
	 */
	public BigDecimal asBigDecimal(String key) {
		String value = get(key);
		if (value == null)
			return BigDecimal.ZERO;
		return new BigDecimal(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.infopeers.restrant.engine.EditableParams#addExtension(java.lang.String
	 * , java.lang.String)
	 */
	public void addExtension(String key, String value) {
		exMultimap.addExtension(key, value);
	}

}