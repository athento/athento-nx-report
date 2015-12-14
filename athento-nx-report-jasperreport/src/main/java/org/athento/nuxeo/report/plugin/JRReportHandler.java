package org.athento.nuxeo.report.plugin;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRExpressionFactory.StringExpressionFactory;

/**
 * Jasper Report handler.
 * 
 * @author victorsanchez
 * 
 */
public class JRReportHandler {

	protected JRDesignStaticText createStaticText(String key, String text,
			int x, int y, int width, int height) {
		JRDesignStaticText staticText = new JRDesignStaticText();
		staticText.setText(text);
		staticText.setKey("staticText-" + key);
		staticText.setX(x);
		staticText.setY(y);
		staticText.setWidth(width);
		staticText.setHeight(height);
		staticText.setMode(ModeEnum.OPAQUE);
		return staticText;
	}

	protected JRDesignVariable createJRVariable(String name) {
		JRDesignVariable variable = new JRDesignVariable();
		variable.setName(name);
		JRExpressionFactory.StringExpressionFactory expressionFactory = new StringExpressionFactory();
		JRExpression expression = (JRExpression) expressionFactory
				.createObject(null);
		variable.setExpression(expression);
		return variable;
	}

	protected JRDesignField createJRField(String name, Class<?> valueClass) {
		return createJRField(name, null, valueClass);
	}

	protected JRDesignField createJRField(String name, String description,
			Class<?> valueClass) {
		JRDesignField field = new JRDesignField();
		field.setName(name);
		if (description != null && !description.isEmpty()) {
			field.setDescription(description);
		}
		field.setValueClass(valueClass);
		return field;
	}

}
