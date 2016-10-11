package globalValues;

import java.math.BigDecimal;
import java.util.Date;

public class Employees {
	String firstName, middleName, lastName, jobPosition, phoneNumber, address, city, state, zipCode, ssn;
	int employeeID, hiredDate;
	BigDecimal wage;
	Boolean isSalary;
	
	public Employees(int id, String firstName, String middleName, String lastName, 
					String phone, String address, String city, String state, 
					String zipCode, int date, String ssn, BigDecimal wage, boolean isSalary){
		this.employeeID = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.phoneNumber = phone;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.hiredDate = date;
		this.ssn = ssn;
		this.wage = wage;
		this.isSalary = isSalary;
	}
	
	public void setID(int id){this.employeeID = id;}
	public void setFirstName(String firstName){this.firstName = firstName;}
	public void setMiddleName(String middleName){this.middleName = middleName;}
	public void setLastName(String lastName){this.lastName = lastName;}
	public void setPhoneNumber(String phone){this.phoneNumber = phone;}
	public void setAddress(String address){this.address = address;}
	public void setCity(String city){this.city = city;}
	public void setState(String state){this.state = state;}
	public void setZipCode(String zipCode){this.zipCode = zipCode;}
	public void setSSN(String ssn){this.ssn = ssn;}
	public void setHiredDate(int date){this.hiredDate = date;}
	public void setWage(BigDecimal wage){this.wage = wage;}
	public void setIsSalary(boolean b){this.isSalary = b;}
	
	public int getID(){return this.employeeID;}
	public String getFirstName(){return this.firstName;}
	public String getMiddleName(){return this.middleName;}
	public String getLastName(){return this.lastName;}
	public String getPhoneNumber(){return this.phoneNumber;}
	public String getAddress(){return this.address;}
	public String getCity(){return this.city;}
	public String getState(){return this.state;}
	public String getZipCode(){return this.zipCode;}
	public String getSSN(){return this.ssn;}
	public int getHiredDate(){return this.hiredDate;}
	public BigDecimal getWage(){return this.wage;}
	public boolean getIsSalary(){return this.isSalary;}
}
