package org.example.ea;

import java.lang.Runnable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.qbicc.graph.ParameterValue;
import org.qbicc.graph.atomic.AccessModes;
import org.qbicc.plugin.opt.ea.EscapeAnalysisFactory;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;

public final class org_example_ea_samples_EASample_01_Basic_sample1_I_I implements Runnable {
  private final String id;

  private final String className;

  private final String methodName;

  public final EscapeAnalysisFactory eaFactory;

  public final EscapeAnalysisIntraMethodBuilder intra;

  public org_example_ea_samples_EASample_01_Basic_sample1_I_I(String id, String className,
      String methodName) {
    this.id = id;
    this.className = className;
    this.methodName = methodName;
    this.eaFactory = EscapeAnalysisFactory.of();
    this.intra = eaFactory.newIntraBuilder(methodName, className);
  }

  public org_example_ea_samples_EASample_01_Basic_sample1_I_I() {
    this("org/example/ea/samples.EASample_01_Basic.sample1(I)I", "org/example/ea/samples/EASample_01_Basic", "sample1");
  }

  public void run() {
    var bbb = intra.getDelegate();
    var params = new ArrayList<ParameterValue>();
    var p0 = bbb.parameter(eaFactory.valueType("s32"), "p", 0);
    params.add(p0);
    intra.startMethod(params);
    var type = eaFactory.definedType("org/example/ea/samples/EASample_01_Basic$A");
    var classObjectType = eaFactory.classObjectType(type, "java/lang/Object");
    var new_ = intra.new_(classObjectType, eaFactory.literalOfType(classObjectType), eaFactory.literalOf("s64 20"), eaFactory.literalOf("s32 8"));
    var ctor = intra.constructorOf(new_, eaFactory.constructorElement(type), eaFactory.methodDescriptor(), eaFactory.functionType());
    intra.call(ctor, List.of());
    var ref = intra.referenceHandle(new_);
    var fieldOf = intra.instanceFieldOf(ref, eaFactory.fieldElement("aField", eaFactory.typeDescriptor("I"), type));
    intra.store(fieldOf, p0, AccessModes.SinglePlain);
    var load = intra.load(fieldOf, AccessModes.SinglePlain);
    intra.return_(load);
    intra.finish();
  }
}
