/**
 * Flexmojos is a set of maven goals to allow maven users to compile, optimize and test Flex SWF, Flex SWC, Air SWF and Air SWC.
 * Copyright (C) 2008-2012  Marvin Froeder <marvin@flexmojos.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.flexmojos.oss.tests.issues;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.marvinformatics.kiss.matchers.file.FileMatchers;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.hamcrest.MatcherAssert;
import net.flexmojos.oss.test.FMVerifier;
import org.testng.annotations.Test;

public class Flexmojos126Test
    extends AbstractIssueTest
{

    @SuppressWarnings( { "unchecked" } )
    @Test
    public void generatorClasspathAccess()
        throws Exception
    {
        FMVerifier v = testIssue( "flexmojos-126" );
        File linkReportXml =
            new File(
                      v.getBasedir(),
                      "FlexMojos-Jira-126-FlexSide/target/FlexMojos-Jira-126-FlexSide-1.0-SNAPSHOT-link-report.xml" );

        MatcherAssert.assertThat( linkReportXml, FileMatchers.exists() );

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read( linkReportXml );

        List<String> linkedFiles = new ArrayList<String>();
        List<Attribute> list = document.selectNodes( "//script/@name" );
        for ( Attribute attribute : list )
        {
            linkedFiles.add( attribute.getValue() );
        }

        assertThat( linkedFiles, hasItems( endsWith( "AdminMessage.as" ), endsWith( "AdminMessageBase.as" ) ) );
    }

}
