/*
package reports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class TodaysTotalSales {

	public TodaysTotalSales(){
		JasperReportBuilder report = DynamicReports.report();
		
		StyleBuilder boldStyle = DynamicReports.stl.style().bold();	
		StyleBuilder boldCentered = DynamicReports.stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		
		TextFieldBuilder<String> title = DynamicReports.cmp.text("End of the Day Sales Order Totals");
		title.setStyle(boldCentered);
		report.title(title);
		
		TextFieldBuilder<String> subTitle = DynamicReports.cmp.text("Sales Order Totals");
		subTitle.setStyle(boldStyle);
		
		TextFieldBuilder<String> cashSubTitle = DynamicReports.cmp.text("Cash:");
		TextFieldBuilder<String> creditSubTitle = DynamicReports.cmp.text("Debit/Credit:");
		TextFieldBuilder<String> numberSales = DynamicReports.cmp.text("Orders:");
		TextFieldBuilder<String> grandTotal = DynamicReports.cmp.text("Grand Total:");
		
		
		try {
			report.show();
		} catch (DRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
*/
