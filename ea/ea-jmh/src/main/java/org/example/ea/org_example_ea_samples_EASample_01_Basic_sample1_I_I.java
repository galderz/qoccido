package org.example.ea;

import java.lang.Runnable;
import java.lang.String;
import java.util.ArrayList;
import org.example.ea.helpers.EscapeAnalysisFactory;
import org.qbicc.graph.ParameterValue;

public final class org_example_ea_samples_EASample_01_Basic_sample1_I_I implements Runnable {
  private final String id;

  private final String className;

  private final String methodName;

  public org_example_ea_samples_EASample_01_Basic_sample1_I_I(String id, String className,
      String methodName) {
    this.id = id;
    this.className = className;
    this.methodName = methodName;
  }

  public org_example_ea_samples_EASample_01_Basic_sample1_I_I() {
    this.id = "org/example/ea/samples.EASample_01_Basic.sample1(I)I";
    this.className = "org/example/ea/samples/EASample_01_Basic";
    this.methodName = "sample1";
  }

  public void run() {
    var eaFactory = EscapeAnalysisFactory.of();
    var intra = eaFactory.newIntraBuilder(methodName, className);
    var bbb = intra.getDelegate();
    var params = new ArrayList<ParameterValue>();
    params.add(bbb.parameter(eaFactory.valueType("s32"), "p", 0));
    intra.startMethod(params);
    var classObjectType = eaFactory.classObjectType("org/example/ea/samples/EASample_01_Basic$A", "java/lang/Object");
    var new_ = intra.new_(classObjectType, eaFactory.literalOfType(classObjectType), eaFactory.literalOf("s64 20"), eaFactory.literalOf("s32 8"));
    var ref = intra.referenceHandle(new_);
    intra.instanceFieldOf("Reference(New(class(org/example/ea/samples/EASample_01_Basic$A),class(org/example/ea/samples/EASample_01_Basic$A),s64 20,s32 8))::class org.qbicc.graph.ReferenceHandle", "org/example/ea/samples.EASample_01_Basic$A.aField::class org.qbicc.type.definition.element.FieldElement");
    intra.load("InstanceFieldOf[Reference(New(class(org/example/ea/samples/EASample_01_Basic$A),class(org/example/ea/samples/EASample_01_Basic$A),s64 20,s32 8))]{org/example/ea/samples.EASample_01_Basic$A.aField}::class org.qbicc.graph.InstanceFieldOf", "SinglePlain::class org.qbicc.graph.atomic.ReadWriteModes$2");
    intra.return_("Load[InstanceFieldOf[Reference(New(class(org/example/ea/samples/EASample_01_Basic$A),class(org/example/ea/samples/EASample_01_Basic$A),s64 20,s32 8))]{org/example/ea/samples.EASample_01_Basic$A.aField}](SinglePlain)::class org.qbicc.graph.Load");
    intra.finish();
  }
}
