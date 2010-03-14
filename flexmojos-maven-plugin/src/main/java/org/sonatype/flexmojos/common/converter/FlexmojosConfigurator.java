package org.sonatype.flexmojos.common.converter;

import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.configurator.BasicComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

@Component( role = ComponentConfigurator.class, hint = "flexmojos" )
public class FlexmojosConfigurator
    extends BasicComponentConfigurator
    implements ComponentConfigurator
{
    
    @Requirement(role=ConfigurationConverter.class, hint="RuledClasses")
    private RuledClassesConverter ruledClassesConverter;

    @Requirement(role=ConfigurationConverter.class, hint="Module")
    private ModuleConverter moduleConverter;
    
    public void configureComponent( Object component, PlexusConfiguration configuration,
                                    ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm,
                                    ConfigurationListener listener )
        throws ComponentConfigurationException
    {
        converterLookup.registerConverter( ruledClassesConverter );
        converterLookup.registerConverter( moduleConverter );

        super.configureComponent( component, configuration, expressionEvaluator, containerRealm, listener );
    }
}