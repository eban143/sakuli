package org.sakuli.services.forwarder.configuration;

import org.apache.commons.lang.StringUtils;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Custom JtwigFunction for abbreviating a provided string to a certain length.
 * The function is using the StringUtils.abbreviate method from the Apache commons library,
 * which is working in the following way:
 *
 * <p>
 * <ul>
 *   <li>If <code>str</code> is less than <code>maxWidth</code> characters long, return it.</li>
 *   <li>Else abbreviate it to <code>(substring(str, 0, max-3) + "...")</code>.</li>
 *   <li>If <code>maxWidth</code> is less than <code>4</code>, throw an
 *       <code>IllegalArgumentException</code>.</li>
 *   <li>In no case the function will return a String of length greater than <code>maxWidth</code>.</li>
 * </ul>
 * </p>
 *
 * <pre>
 * StringUtils.abbreviate(null, *)      = null
 * StringUtils.abbreviate("", 4)        = ""
 * StringUtils.abbreviate("abcdefg", 6) = "abc..."
 * StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
 * StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
 * StringUtils.abbreviate("abcdefg", 4) = "a..."
 * StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
 * </pre>
 *
 * @author Georgi Todorov
 */
public class AbbreviateFunction extends SimpleJtwigFunction {

    private Map<String, String> formatExpressions;

    public AbbreviateFunction(Map<String, String> formatExpressions) {
        this.formatExpressions = formatExpressions;
    }

    @Override
    public String name() {
        return "abbreviate";
    }

    @Override
    public Object execute(FunctionRequest request) {
        assert request.getNumberOfArguments() == 2;
        String toAbbreviate = (String) request.getArguments().get(0);
        BigDecimal summaryMaxLength = (BigDecimal) request.getArguments().get(1);
        return StringUtils.abbreviate(toAbbreviate, summaryMaxLength.intValue());
    }

}
