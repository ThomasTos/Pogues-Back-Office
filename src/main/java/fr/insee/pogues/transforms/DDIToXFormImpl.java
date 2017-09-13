package fr.insee.pogues.transforms;

import fr.insee.eno.GenerationService;
import fr.insee.eno.generation.DDI2FRGenerator;
import fr.insee.eno.postprocessing.NoopPostprocessor;
import fr.insee.eno.preprocessing.DDIPreprocessor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class DDIToXFormImpl implements DDIToXForm {

    @Override
    public void transform(InputStream input, OutputStream output, Map<String, Object> params) throws Exception {
        if (null == input) {
            throw new NullPointerException("Null input");
        }
        if (null == output) {
            throw new NullPointerException("Null output");
        }
        String xform = transform(input, params);
        output.write(xform.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String transform(InputStream input, Map<String, Object> params) throws Exception {
        if (null == input) {
            throw new NullPointerException("Null input");
        }
        File enoInput = null;
        enoInput = File.createTempFile("eno", ".xml");
        FileUtils.copyInputStreamToFile(input, enoInput);
        return transform(enoInput, params);
    }

    @Override
    public String transform(String input, Map<String, Object> params) throws Exception {
        File enoInput = null;
        if (null == input) {
            throw new NullPointerException("Null input");
        }
        enoInput = File.createTempFile("eno", ".xml");
        FileUtils.writeStringToFile(enoInput, input, StandardCharsets.UTF_8);
        return transform(enoInput, params);
    }

    private String transform(File file, Map<String, Object> params) throws Exception {
        File output = null;
        GenerationService genService = new GenerationService(new DDIPreprocessor(), new DDI2FRGenerator(), new NoopPostprocessor());
        output = genService.generateQuestionnaire(file, null);
        String response = FileUtils.readFileToString(output, Charset.forName("UTF-8"));
        return response;
    }
}
