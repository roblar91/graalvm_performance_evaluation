import matplotlib.pyplot as plt
import toml
import os

WORKING_DIRECTORY = '/home/knickus/graalvm_performance/results/2020-04-15/charts'
TOML_FILE_PATH = "/home/knickus/graalvm_performance/results/2020-04-15/results.toml"

BENCHMARKS = ["AVRORA",
              "FOP",
              "H2",
              "JYTHON",
              "LUINDEX",
              "LUSEARCH_FIX",
              "PMD",
              "SUNFLOW",
              "TRADEBEANS",
              "XALAN"]

JVMS = ["OPENJDK_8",
        "ORACLEJDK_8",
        "GRAALVM_CE_8",
        "GRAALVM_EE_8",
        "OPENJDK_11",
        "ORACLEJDK_11",
        "GRAALVM_CE_11",
        "GRAALVM_EE_11"]

MEASUREMENTS = ["STARTUP",
                "STEADY_STATE"]


def create_chart(benchmark, measurement, data, normalization_target_jvm):
    benchmark_data = dict(data.get(benchmark))
    normalization_value = dict(dict(benchmark_data.get(normalization_target_jvm)).get(measurement)).get("mean")
    normalization_error = dict(dict(benchmark_data.get(normalization_target_jvm)).get(measurement)).get("error")
    confidence_level = dict(dict(benchmark_data.get(normalization_target_jvm)).get(measurement)).get("confidence_level")
    size = dict(dict(benchmark_data.get(normalization_target_jvm)).get(measurement)).get("size")

    hlines = [1 + normalization_error / normalization_value, 1 - normalization_error / normalization_value]
    ticks = JVMS
    x = []
    means = []
    errors = []
    mins = []
    maxs = []

    for jvm in JVMS:
        jvm_data = dict(dict(benchmark_data.get(jvm)).get(measurement))
        x.append(len(x) + 1)
        means.append(jvm_data.get("mean") / normalization_value)
        errors.append(jvm_data.get("error") / normalization_value)
        mins.append(jvm_data.get("min") / normalization_value)
        maxs.append(jvm_data.get("max") / normalization_value)

    plt.figure(figsize=(len(x) * 1.5, len(x)), dpi=200)

    plt.xticks(x,
               ticks,
               rotation=90)

    plt.vlines(x=x,
               ymax=maxs,
               ymin=mins,
               colors="darkgrey",
               linestyles="dotted")

    plt.hlines(y=hlines,
               xmin=0,
               xmax=len(x) + 1,
               linestyles="dotted",
               colors="black")

    plt.errorbar(x=x,
                 y=means,
                 yerr=errors,
                 color="black",
                 capsize=5,
                 capthick=2,
                 linestyle="None",
                 marker="s",
                 markersize=5,
                 elinewidth=2)

    for i in range(len(x)):
        plt.annotate("%.3f" % means[i],
                     (x[i], means[i]),
                     xytext=(x[i] + 0.1, means[i] + 0.02),
                     xycoords="data",
                     arrowprops=dict(arrowstyle="->",
                                     color="0.5",
                                     connectionstyle="angle3, angleA=90, angleB=0"))

    plt.title(benchmark + " " + measurement + " at " + confidence_level + " confidence level\n"
              + "Sample size: " + str(size))
    plt.ylabel("Normalized execution time")
    plt.tight_layout()
    filename = benchmark + "_" + measurement
    plt.savefig(fname=filename)


os.makedirs(WORKING_DIRECTORY, exist_ok=True)
os.chdir(WORKING_DIRECTORY)
results = toml.load(TOML_FILE_PATH)

for bm in BENCHMARKS:
    for mm in MEASUREMENTS:
        create_chart(bm, mm, results, JVMS[0])
