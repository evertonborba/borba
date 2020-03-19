package br.com.ecborba.dataanalysis.helper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.ecborba.dataanalysis.function.DataFunctions;
import br.com.ecborba.dataanalysis.model.Customer;
import br.com.ecborba.dataanalysis.model.Sale;
import br.com.ecborba.dataanalysis.model.Seller;
import br.com.ecborba.dataanalysis.util.Constants;
import br.com.ecborba.dataanalysis.util.DataType;

public class DataHelper {

	public static boolean isCorrectFileType(Path file) {
		return (file.toString().endsWith(Constants.IN_FILE_EXT));
	}

	public static void createFileOut(List<String> lines, Path path) throws IOException {
	
		String fileName = Constants.DEFAULT_OUT_DIR
				+ path.getFileName().toString().replace(Constants.IN_FILE_EXT, Constants.OUT_FILE_EXT);
	
		Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);
	}

	public static void processDataFromFiles(Path path) {
		List<Seller> listSeller = new ArrayList<>();
		List<Customer> listCustomer = new ArrayList<>();
		List<Sale> listSales = new ArrayList<>();
	
		try {
	
			try (Stream<String> linhas = Files.lines(path, StandardCharsets.UTF_8)) {
	
				listSeller = linhas.filter(line -> line.startsWith(DataType.SELLER.getIdData()))
						.map(DataFunctions.mapLineToSeller).collect(Collectors.toList());
			}
	
			try (Stream<String> linhas = Files.lines(path, StandardCharsets.UTF_8)) {
	
				listCustomer = linhas.filter(line -> line.startsWith(DataType.CUSTOMER.getIdData()))
						.map(DataFunctions.mapLineToCustomer).collect(Collectors.toList());
			}
	
			try (Stream<String> linhas = Files.lines(path, StandardCharsets.UTF_8)) {
	
				listSales = linhas.filter(line -> line.startsWith(DataType.SALES.getIdData()))
						.map(DataFunctions.mapLineToSale).collect(Collectors.toList());
			}
	
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	
		List<String> lines = new ArrayList<>();
	
		// Quantidade de clientes no arquivo de entrada
		Integer customerQuantity = listCustomer.size();
		lines.add("Number of clients in the input file is : " + customerQuantity);
		System.out.println("Number of clients in the input file is : " + customerQuantity);
	
		// Quantidade de vendedores no arquivo de entrada
		Integer sellerQuantity = listSeller.size();
		lines.add("Number of sellers in the input file : " + sellerQuantity);
		System.out.println("Number of sellers in the input file : " + sellerQuantity);
	
		// ID da venda mais cara
		Sale sale = Collections.max(listSales, Comparator.comparing(s -> s.getSaleTotal()));
		lines.add("Most expensive sale ID is : " + sale.getSaleId());
		System.out.println("Most expensive sale ID is : " + sale.getSaleId());
	
		// O pior vendedor
		sale = Collections.min(listSales, Comparator.comparing(s -> s.getSaleTotal()));
		lines.add("The worst seller is : " + sale.getSalesmanName());
		System.out.println("The worst seller is : " + sale.getSalesmanName());
	
		try {
			createFileOut(lines, path);
		} catch (IOException e) {
			System.out.println("Exception:" + e.toString());
			e.printStackTrace();
		}
	}

}
