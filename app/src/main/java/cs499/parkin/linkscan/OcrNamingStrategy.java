package cs499.parkin.linkscan;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * Created by parkin on 2/8/2016.
 */
public class OcrNamingStrategy extends PropertyNamingStrategy {

    @Override
    public String nameForField(MapperConfig
                                       config,
                               AnnotatedField field, String defaultName) {
        return convert(defaultName);

    }
    @Override
    public String nameForGetterMethod(MapperConfig
                                              config,
                                      AnnotatedMethod method, String defaultName) {
        return convert(defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig
                                              config,
                                      AnnotatedMethod method, String defaultName) {
        String a = convert(defaultName);
        return a;
    }

    public String convert(String defaultName )
    {
        //rare cases
        if(defaultName.toLowerCase().equals("ocrexitcode")){
            return "OCRExitCode";
        }

        char[] arr = defaultName.toCharArray();
        if(arr.length !=0)
        {
            if ( Character.isLowerCase(arr[0])){
                char upper = Character.toUpperCase(arr[0]);
                arr[0] = upper;
            }
        }
        return new StringBuilder().append(arr).toString();
    }
}
