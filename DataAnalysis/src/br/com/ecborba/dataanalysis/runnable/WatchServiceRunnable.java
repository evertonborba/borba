package br.com.ecborba.dataanalysis.runnable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import br.com.ecborba.dataanalysis.helper.DataHelper;
import br.com.ecborba.dataanalysis.util.Constants;

public class WatchServiceRunnable implements Runnable {

	WatchService watchService;

	public WatchServiceRunnable(WatchService myWatchService) {
		watchService = myWatchService;
	}

	@Override
	public void run() {
		try {

			System.out.println("\nMy Runnable Waiting on Events!\n");

			WatchKey key = watchService.take();

			while (key != null) {

				// Sleep for 5 seconds to eliminate the duplicate event
				Thread.sleep(5000);

				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					Kind<?> kind = watchEvent.kind();

					System.out.println("New Event: " + watchEvent.context() + ", count: " + watchEvent.count()
							+ ", event: " + watchEvent.kind());

					System.out.println("New Event: " + watchEvent.context() + " --> " + kind.toString());

					if (StandardWatchEventKinds.ENTRY_CREATE.equals(kind)) {
						WatchEvent<?> pathEvent = (WatchEvent<?>) watchEvent;
						Path fileName = (Path) pathEvent.context();
						if (DataHelper.isCorrectFileType(fileName)) {
							System.out.println("File Created:" + fileName);
							Path path = Paths.get(Constants.DEFAULT_IN_DIR + "\\" + fileName.getFileName().toString());
							DataHelper.processDataFromFiles(path);
						}
					} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind)) {
						WatchEvent<?> pathEvent = (WatchEvent<?>) watchEvent;
						Path fileName = (Path) pathEvent.context();
						if (DataHelper.isCorrectFileType(fileName)) {
							System.out.println("File Updated:" + fileName);
							Path path = Paths.get(Constants.DEFAULT_IN_DIR + "\\" + fileName.getFileName().toString());
							DataHelper.processDataFromFiles(path);
						}
					}
				}

				// Make sure to reset the event that just happened
				key.reset();
				key = watchService.take();
			}

		} catch (Exception e) {
			System.out.println("Exception:" + e.toString());
			e.printStackTrace();
		}
	}

}
