package lv.dsns.support24.common.dataloader;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@Profile({"!prod"})
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ApplicationDataLoader {

    UserLoader userLoader;
    UnitLoader unitLoader;
    ProblemLoader problemLoader;
    RankLoader rankLoader;

    static final String DATA_LOADER_CLASSPATH_LOCATION = "classpath:dataloader";

    public void load() {
        String units = getDataLoaderJsonFullPath("units");
        String problems = getDataLoaderJsonFullPath("problems");
        String users = getDataLoaderJsonFullPath("users");
        String ranks = getDataLoaderJsonFullPath("ranks");

        loadData(units, unitLoader);
        loadData(problems,problemLoader);
        loadData(users, userLoader);
        loadData(ranks, rankLoader);

    }

    private String getDataLoaderJsonFullPath(String folders) {
        return String.format("%s/%s/*.json", DATA_LOADER_CLASSPATH_LOCATION, folders);
    }

    private void loadData(String locationPattern, Consumer<List<Map<String, Object>>> dataConsumer) {
        loadData(locationPattern, dataConsumer, null);
    }

    private void loadData(String locationPattern, Consumer<List<Map<String, Object>>> dataLoaderConsumer, Consumer<List<Map<String, Object>>> dataCleanUpHandlerConsumer) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(locationPattern);
            List<Map<String, Object>> resourceObjectMaps = new ArrayList<>();
            for (Resource resource : resources) {
                JsonParser jsonParser = JsonParserFactory.getJsonParser();

                try (InputStream in = resource.getInputStream()) {
                    Map<String, Object> objectMap = jsonParser.parseMap(IOUtils.toString(in, StandardCharsets.UTF_8));
                    resourceObjectMaps.add(objectMap);
                }
            }

            dataLoaderConsumer.accept(resourceObjectMaps);

            if (dataCleanUpHandlerConsumer != null) {
                dataCleanUpHandlerConsumer.accept(resourceObjectMaps);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
