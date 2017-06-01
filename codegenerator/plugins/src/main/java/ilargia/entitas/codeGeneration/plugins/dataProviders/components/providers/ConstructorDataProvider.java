package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.IComponentDataProvider;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;
import java.util.stream.Collectors;


public class ConstructorDataProvider implements IComponentDataProvider {

    public static String CONSTRUCTOR_INFOS = "constructorInfos";

    public static List<MethodSource<JavaClassSource>> getConstructorData(SourceDataFile data) {
        return (List<MethodSource<JavaClassSource>>) data.get(CONSTRUCTOR_INFOS);
    }

    public static void setConstructorData(SourceDataFile data, List<MethodSource<JavaClassSource>> contructores) {
        data.put(CONSTRUCTOR_INFOS, contructores);
    }

    @Override
    public void provide(SourceDataFile data) {
        List<MethodSource<JavaClassSource>> contructores = data.getFileContent().getMethods().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isConstructor())
//                .filter(method -> method.getParameters().size() > 0)
                .collect(Collectors.toList());
        setConstructorData(data, contructores);
    }

}