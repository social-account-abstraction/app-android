package md.hackaton.aasocialrecovery.account;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AbiCoder {

    public static byte[] encode(List<String> encTypes, List<Object> encValues) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < encTypes.size(); i++) {
            Class<Type> typeClazz = (Class<Type>) AbiTypes.getType(encTypes.get(i));
            boolean atleastOneConstructorExistsForGivenParametersType = false;
            // Using the Reflection API to get the types of the parameters
            Constructor[] constructors = typeClazz.getConstructors();
            for (Constructor constructor : constructors) {
                // Check which constructor matches
                try {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    byte[] temp =
                            Numeric.hexStringToByteArray(
                                    TypeEncoder.encode(
                                            typeClazz
                                                    .getDeclaredConstructor(parameterTypes)
                                                    .newInstance(encValues.get(i))));
                    baos.write(temp, 0, temp.length);
                    atleastOneConstructorExistsForGivenParametersType = true;
                    break;
                } catch (IllegalArgumentException
                         | NoSuchMethodException
                         | InstantiationException
                         | IllegalAccessException
                         | InvocationTargetException ignored) {
                }
            }

            if (!atleastOneConstructorExistsForGivenParametersType) {
                throw new RuntimeException(
                        String.format(
                                "Received an invalid argument for which no constructor exists for the ABI Class %s. index = " + i,
                                typeClazz.getSimpleName()
                        ));
            }
        }

        return baos.toByteArray();
    }
}
