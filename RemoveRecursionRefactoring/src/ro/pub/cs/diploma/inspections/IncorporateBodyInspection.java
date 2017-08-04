package ro.pub.cs.diploma.inspections;

public class IncorporateBodyInspection extends DummyInspection {
  @Override
  protected String getKey() {
    return "incorporate.body";
  }

  @Override
  protected int getSteps() {
    return 6;
  }
}