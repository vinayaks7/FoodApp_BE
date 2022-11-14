package com.tutorial.FoodApp.serviceImplementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.tutorial.FoodApp.JWT.JwtFilter;
import com.tutorial.FoodApp.dao.BillDao;
import com.tutorial.FoodApp.model.Bill;
import com.tutorial.FoodApp.service.BillService;
import com.tutorial.FoodApp.utils.FoodAppUtils;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tutorial.FoodApp.constants.FoodAppConstants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
    JwtFilter jwtFilter;

	@Autowired
    BillDao billDao;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {

			String fileName;

			if (validateRequestMap(requestMap)) {
				// If data is already in the DB
				if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {

					fileName = (String) requestMap.get("uuid");
				} else {
					fileName = FoodAppUtils.getUUID();
					requestMap.put("uuid", fileName);
					insertBill(requestMap);
				}

				String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: "
						+ requestMap.get("contactNumber") + "\n" + "Email: " + requestMap.get("email") + "\n"
						+ "Payment Method: " + requestMap.get("paymentMethod");
				Document document = new Document();
				PdfWriter.getInstance(document,
						new FileOutputStream(FoodAppConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

				document.open();

				// Bill heading
				Paragraph chunk = new Paragraph("Spring Boot Food Application", getFont("Header"));
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);

				// Customer data
				Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
				document.add(paragraph);

				// Creating tables
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);

				// Adding data in tables
				JSONArray jsonArray = FoodAppUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));

				for (int i = 0; i < jsonArray.length(); i++) {
					addRows(table, FoodAppUtils.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);

				Paragraph footer = new Paragraph("Total: " + requestMap.get("totalAmount"), getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
			} else {
				return FoodAppUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return FoodAppUtils.getResponseEntity(FoodAppConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private void addRows(PdfPTable table, Map<String, Object> data) {
		// TODO Auto-generated method stub
		table.addCell((String) data.get("name"));
		table.addCell((String) data.get("category"));
		table.addCell((String) data.get("quantity"));
		table.addCell(Double.toString((Double) data.get("price")));
		table.addCell(Double.toString((Double) data.get("total")));
	}

	private void addTableHeader(PdfPTable table) {
		// TODO Auto-generated method stub
		Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(1);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.YELLOW);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});
	}

	private Font getFont(String type) {
		switch (type) {
		case "Header":
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		case "Data":
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
		default:
			return new Font();
		}
	}

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String) requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt(requestMap.get("totalAmount").toString()));
			bill.setProductDetails((String) requestMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billDao.save(bill);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
		return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
				&& requestMap.containsKey("productDetails") && requestMap.containsKey("totalAmount");
	}

	@Override
	public ResponseEntity<List<Bill>> getBills() {
		List<Bill> list = new ArrayList<>();
		if (jwtFilter.isBrancManager()) {
			list = billDao.getAllBills();
		} else {
			list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	//Creating and downloading PDF from the data already present in the database
	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		try {
			byte[] byteArray = new byte[0];
			if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
			} else {
				String filePath = FoodAppConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
				
				if(FoodAppUtils.doesFileExist(filePath)) {
					byteArray = getByteArray(filePath);
					return new ResponseEntity<>(byteArray, HttpStatus.OK);
				} else {
					requestMap.put("isGenerate", false);
					generateReport(requestMap);
					byteArray = getByteArray(filePath);
					return new ResponseEntity<>(byteArray, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	//
	private byte[] getByteArray(String filePath) throws Exception{
		File initialFile = new File(filePath);
		InputStream targetStream = new FileInputStream(initialFile);
		byte[] byteArray = IOUtils.toByteArray(targetStream);
		targetStream.close();
		return byteArray;
	}

}
