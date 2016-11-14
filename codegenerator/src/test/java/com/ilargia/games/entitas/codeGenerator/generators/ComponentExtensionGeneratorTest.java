package com.ilargia.games.entitas.codeGenerator.generators;

import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ComponentExtensionGeneratorTest {

    private ComponentExtensionsGenerator generator;
    private String[] poolNames;

    @Before
    public void setUp() throws Exception {
        generator = new ComponentExtensionsGenerator();
        poolNames = new String[]{"pruebas", "rubentxu"};

    }

    @Test
    public void generateComponentInfos() {
        ComponentInfo[] componentInfos = new ComponentInfo[2];
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        List<FieldSource<JavaClassSource>> memberInfos2 = new ArrayList<>();
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        final FieldSource<JavaClassSource> field = javaClass.addField("public float x;");
        final FieldSource<JavaClassSource> field2 = javaClass.addField("public float y;");
        final FieldSource<JavaClassSource> field3 = javaClass.addField("public boolean isMovable;");
        memberInfos.add(field);
        memberInfos.add(field2);
        memberInfos2.add(field3);

        componentInfos[0] = new ComponentInfo("com.ilargia.games.entitas.components.Position", "Position", memberInfos, new String[]{"pruebas"},
                false, "", true, true, false, false);
        componentInfos[1] = new ComponentInfo("com.ilargia.games.entitas.components.Movable", "Movable", null, new String[]{"pruebas"},
                true, "", true, true, false, false);

        List<JavaClassSource> result = generator.generate(componentInfos, "com.pruebas.entitas");

        assertEquals(2, result.size());

    }


}