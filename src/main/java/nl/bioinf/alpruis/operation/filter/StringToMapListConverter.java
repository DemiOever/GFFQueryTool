package nl.bioinf.alpruis.operation.filter;
import nl.bioinf.alpruis.ErrorThrower;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringToMapListConverter implements CommandLine.ITypeConverter<Map<String, List<String>>> {
    @Override
    public Map<String, List<String>> convert(String value) {
        Map<String, List<String>> map = new HashMap<>();
        try {
            map.put(value.split("=")[0], List.of(value.split("=")[1].split(",")));
        } catch (Exception e) {
            ErrorThrower.throwErrorE(e);
        }
        return map;
    }
}
