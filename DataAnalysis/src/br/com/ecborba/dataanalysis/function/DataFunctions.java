package br.com.ecborba.dataanalysis.function;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.base.Splitter;

import br.com.ecborba.dataanalysis.model.Customer;
import br.com.ecborba.dataanalysis.model.Item;
import br.com.ecborba.dataanalysis.model.Sale;
import br.com.ecborba.dataanalysis.model.Seller;

public class DataFunctions {

	public static Function<String, Seller> mapLineToSeller = new Function<String, Seller>() {

		public Seller apply(String line) {

			Seller seller = new Seller();

			List<String> sellerPieces = Splitter.on("ç").trimResults().omitEmptyStrings().splitToList(line);

			seller.setCpf(sellerPieces.get(1));
			seller.setName(sellerPieces.get(2));
			seller.setSalary(new BigDecimal(sellerPieces.get(3)));

			return seller;
		}
	};

	public static Function<String, Customer> mapLineToCustomer = new Function<String, Customer>() {

		public Customer apply(String line) {

			Customer customer = new Customer();

			List<String> customerPieces = Splitter.on("ç").trimResults().omitEmptyStrings().splitToList(line);

			customer.setCnpj(customerPieces.get(1));
			customer.setName(customerPieces.get(2));
			customer.setBusinessArea(customerPieces.get(3));

			return customer;
		}
	};

	public static Function<String, Sale> mapLineToSale = new Function<String, Sale>() {

		public Sale apply(String line) {

			Sale sale = new Sale();

			List<String> salePieces = Splitter.on("ç").trimResults().omitEmptyStrings().splitToList(line);

			sale.setSaleId(salePieces.get(1));
			sale.setSalesmanName(salePieces.get(3));

			List<String> itemsPieces = Splitter.on(",").trimResults().omitEmptyStrings()
					.splitToList(salePieces.get(2).replace("[", "").replace("]", ""));

			List<Item> items = new ArrayList<>();
			for (String string : itemsPieces) {
				List<String> itemPieces = Splitter.on("-").trimResults().omitEmptyStrings().splitToList(string);

				Item item = new Item();
				item.setItemId(new BigInteger(itemPieces.get(0)));
				item.setItemQuantity(new BigInteger(itemPieces.get(1)));
				item.setItemPrice(new BigDecimal(itemPieces.get(2)));

				MathContext mc = new MathContext(4);
				item.setItemTotal(item.getItemPrice().multiply(new BigDecimal(item.getItemQuantity()), mc));

				items.add(item);
			}

			sale.setItems(items);

			BigDecimal sum = items.stream().map(Item::getItemTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

			sale.setSaleTotal(sum);

			return sale;
		}
	};

}
