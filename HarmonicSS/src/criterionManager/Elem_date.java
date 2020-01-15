package criterionManager;

public class Elem_date {
	
	public String specific_year = "";
	public String specific_month = "";
	public String specific_day = "";
	
	public String preiod_begin_year = "";
	public String preiod_begin_month = "";
	public String preiod_begin_day = "";
	
	public String preiod_end_year = "";
	public String preiod_end_month = "";
	public String preiod_end_day = "";
	
	public String until_year = "";
	public String until_date_month = "";
	public String until_date_day = "";
	
	
	public Elem_date() {
		this.specific_year = "";
		this.specific_month = "";
		this.specific_day = "";
		
		this.preiod_begin_year = "";
		this.preiod_begin_month = "";
		this.preiod_begin_day = "";
		
		this.preiod_end_year = "";
		this.preiod_end_month = "";
		this.preiod_end_day = "";
		
		this.until_year = "";
		this.until_date_month = "";
		this.until_date_day = "";
	}
	
	public Elem_date(String specific_year, String specific_month, String specific_day, String preiod_begin_year, String preiod_begin_month,
			String preiod_begin_day, String preiod_end_year, String preiod_end_month, String preiod_end_day, String until_year,
			String until_month, String until_day) {
		
		this.specific_year = specific_year;
		this.specific_month = specific_month;
		this.specific_day = specific_day;
		
		this.preiod_begin_year = preiod_begin_year;
		this.preiod_begin_month = preiod_begin_month;
		this.preiod_begin_day = preiod_begin_day;
		
		this.preiod_end_year = preiod_end_year;
		this.preiod_end_month = preiod_end_month;
		this.preiod_end_day = preiod_end_day;
		
		this.until_year = until_year;
		this.until_date_month = until_month;
		this.until_date_day = until_day;
	}

	public String getSpecific_year() {
		return specific_year;
	}

	public void setSpecific_year(String specific_year) {
		this.specific_year = specific_year;
	}

	public String getSpecific_month() {
		return specific_month;
	}

	public void setSpecific_month(String specific_month) {
		this.specific_month = specific_month;
	}

	public String getSpecific_day() {
		return specific_day;
	}

	public void setSpecific_day(String specific_day) {
		this.specific_day = specific_day;
	}

	public String getPreiod_begin_year() {
		return preiod_begin_year;
	}

	public void setPreiod_begin_year(String preiod_begin_year) {
		this.preiod_begin_year = preiod_begin_year;
	}

	public String getPreiod_begin_month() {
		return preiod_begin_month;
	}

	public void setPreiod_begin_month(String preiod_begin_month) {
		this.preiod_begin_month = preiod_begin_month;
	}

	public String getPreiod_begin_day() {
		return preiod_begin_day;
	}

	public void setPreiod_begin_day(String preiod_begin_day) {
		this.preiod_begin_day = preiod_begin_day;
	}

	public String getPreiod_end_year() {
		return preiod_end_year;
	}

	public void setPreiod_end_year(String preiod_end_year) {
		this.preiod_end_year = preiod_end_year;
	}

	public String getPreiod_end_month() {
		return preiod_end_month;
	}

	public void setPreiod_end_month(String preiod_end_month) {
		this.preiod_end_month = preiod_end_month;
	}

	public String getPreiod_end_day() {
		return preiod_end_day;
	}

	public void setPreiod_end_day(String preiod_end_day) {
		this.preiod_end_day = preiod_end_day;
	}

	public String getUntil_year() {
		return until_year;
	}

	public void setUntil_year(String until_year) {
		this.until_year = until_year;
	}

	public String getUntil_date_month() {
		return until_date_month;
	}

	public void setUntil_date_month(String until_date_month) {
		this.until_date_month = until_date_month;
	}

	public String getUntil_date_day() {
		return until_date_day;
	}

	public void setUntil_date_day(String until_date_day) {
		this.until_date_day = until_date_day;
	}


	

}
