package net.lenni0451.commons.asm.info;

import javax.annotation.Nullable;
import java.util.Set;

public interface ClassInfo {

    /**
     * @return The modifiers of the class
     */
    int getModifiers();

    /**
     * @return The internal name of the class
     */
    String getName();

    /**
     * @return The internal name of the super class (null if the class is {@link Object})
     */
    @Nullable
    String getSuperClass();

    /**
     * @return The resolved {@link ClassInfo} of the super class
     */
    ClassInfo getSuperClassInfo();

    /**
     * @return The internal names of the interfaces
     */
    String[] getInterfaces();

    /**
     * @return The resolved {@link ClassInfo} of the interfaces
     */
    ClassInfo[] getInterfaceInfos();

    /**
     * @return The resolved {@link ClassInfo}s of all super classes and interfaces not including the current class
     */
    Set<ClassInfo> getRecursiveSuperClasses();

    /**
     * @return The fields of the class
     */
    FieldInfo[] getFields();

    /**
     * @return The methods of the class
     */
    MethodInfo[] getMethods();

}
