package org.athento.nuxeo.report.test;

import org.athento.nuxeo.report.api.model.BasicReportData;

/**
 * Sample report bean.
 * 
 * @author victorsanchez
 * 
 */
public class SampleReportBean extends BasicReportData {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = -8753865831910149171L;

	public String name;

	public int age;

	public String address;

	/**
	 * Me.
	 * @return
	 */
	public SampleReportBean getMe() {
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SampleReportBean [name=" + name + ", age=" + age + ", address="
				+ address + "]";
	}

}
