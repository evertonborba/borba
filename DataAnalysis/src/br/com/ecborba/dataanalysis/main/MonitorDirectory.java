package br.com.ecborba.dataanalysis.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.stream.Stream;

import br.com.ecborba.dataanalysis.helper.DataHelper;
import br.com.ecborba.dataanalysis.runnable.WatchServiceRunnable;
import br.com.ecborba.dataanalysis.util.Constants;

public class MonitorDirectory {

	public static void main(String[] args) {

		readingPreviouslyExistingFiles();

		readingActiveContinuously();

	}

	private static void readingActiveContinuously() {
		// leitura ativa ininterruptamente

		Path toWatch = Paths.get(Constants.DEFAULT_IN_DIR);

		try {

			System.out.println("Starting Monitor Directory!");

			WatchService watchService = toWatch.getFileSystem().newWatchService();

			WatchServiceRunnable watchSvcRunnable = new WatchServiceRunnable(watchService);
			Thread th = new Thread(watchSvcRunnable, "WatchServiceRunnable");
			th.start();

			// Register to watch for created, deleted, and modified events
			toWatch.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			System.out.println("Exception:" + e.toString());
			e.printStackTrace();
		}
	}

	private static void readingPreviouslyExistingFiles() {
		// leitura de arquivos previamente existentes
		try (Stream<Path> paths = Files.walk(Paths.get(Constants.DEFAULT_IN_DIR))) {

			paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(Constants.IN_FILE_EXT))
					.forEach(path -> {
						System.out.println("File Readed:" + path);
						DataHelper.processDataFromFiles(path);
					});

		} catch (IOException e) {
			System.out.println("Exception:" + e.toString());
			e.printStackTrace();
		}
	}

}
