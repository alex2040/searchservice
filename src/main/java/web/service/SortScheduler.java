package web.service;

import filesearch.source.file.FileSourceSorter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import web.Config;

import java.io.IOException;

@Component
public class SortScheduler {

    private static final int ONE_MINUTE = 60_000;
    private Config config;

    public SortScheduler(Config config) {
        this.config = config;
    }

    @Scheduled(fixedDelay = ONE_MINUTE)
    public void sortFiles() {
        try {
            FileSourceSorter.INSTANCE.sort(config.getSearchPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
