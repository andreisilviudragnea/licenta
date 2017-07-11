/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pub.cs.diploma;

import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.IGQuickFixesTestCase;
import com.siyeh.ig.fixes.performance.RemoveTailRecursionFixTest;
import com.siyeh.ig.performance.TailRecursionInspection;

/**
 * @see RemoveTailRecursionFixTest
 */
public class RemoveRecursionFixTest extends IGQuickFixesTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.enableInspections(new TailRecursionInspection());
    myRelativePath = "fixes";
    myDefaultHint = InspectionGadgetsBundle.message("tail.recursion.replace.quickfix");
  }

  @Override
  protected String getTestDataPath() {
    return "testdata";
  }

  public void testCallOnOtherInstance1() { doTest(); }
  public void testCallOnOtherInstance2() { doTest(); }
  public void testDependency1() { doTest(); }
  public void testDependency2() { doTest(); }
  public void testDependency3() { doTest(); }
  public void testDependency4() { doTest(); }
  public void testThisVariable() { doTest(); }
  public void testUnmodifiedParameter() { doTest(); }
}