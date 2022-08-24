package virtualdevelopercloud.origin_app_v;

import android.app.Application;
import android.os.StrictMode;

import org.matomo.sdk.Tracker;
import org.matomo.sdk.TrackerBuilder;
import org.matomo.sdk.extra.DimensionQueue;
import org.matomo.sdk.extra.DownloadTracker;
import org.matomo.sdk.extra.MatomoApplication;
import org.matomo.sdk.extra.TrackHelper;



import org.matomo.sdk.TrackerBuilder;


public class App extends MatomoApplication {

    private TrackerBuilder tracker;

    @Override
    public TrackerBuilder onCreateTrackerConfig() {
        tracker = TrackerBuilder.createDefault("https://productracker.com/analiticas/matomo.php", 1);
        return tracker;
    }
}
