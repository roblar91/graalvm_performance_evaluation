import matplotlib.pyplot as plt
from matplotlib import colors
from matplotlib.pyplot import cm
import toml
import os
import numpy as np

WORKING_DIRECTORY = '/home/knickus/graalvm_performance/results/2020-04-15/heatmaps'
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


def compare_performance(first: dict, second: dict):
    """Returns 1 if first performs better than second,
       returns 0 if no conclusion can be made.
       returns -1 if first performs worse than second"""

    if first == second:
        return 0

    first_mean = first.get("mean")
    first_error = first.get("error")
    first_upper = first_mean + first_error
    first_lower = first_mean - first_error

    second_mean = second.get("mean")
    second_error = second.get("error")
    second_upper = second_mean + second_error
    second_lower = second_mean - second_error

    if first_upper < second_lower:
        return 1
    elif first_lower > second_upper:
        return -1

    return 0


def create_heatmap(benchmark, state, matrix, confidence_level):
    fig, ax = plt.subplots()
    cmap = colors.ListedColormap(['red', 'grey', 'blue'])
    ax.imshow(matrix, cmap=cm.get_cmap("coolwarm"))
    ax.set_xticks(np.arange(len(JVMS)))
    ax.set_yticks(np.arange(len(JVMS)))
    ax.set_xticklabels(JVMS)
    ax.set_yticklabels(JVMS)
    ax.xaxis.tick_top()
    plt.setp(ax.get_xticklabels(), rotation=45, ha="left", rotation_mode="anchor")

    for i in range(len(matrix)):
        for j in range(len(matrix[i])):
            text = ax.text(j,
                           i,
                           matrix[i][j],
                           ha="center",
                           va="center",
                           color="w")

    plt.title(benchmark + " " + state + "\n" + confidence_level + " confidence level")
    fig.tight_layout()
    filename = benchmark + "_" + state + "_heatmap"
    plt.savefig(filename)
    plt.close(fig)


def create_empty_matrix(size):
    rows = []
    for i in range(size):
        cols = []
        rows.append(cols)
        for j in range(size):
            cols.append(0)

    return rows


os.makedirs(WORKING_DIRECTORY, exist_ok=True)
os.chdir(WORKING_DIRECTORY)
data = toml.load(TOML_FILE_PATH)

for s in MEASUREMENTS:
    cl = ""
    total = create_empty_matrix(len(JVMS))
    for bm in BENCHMARKS:
        m = []
        for id_jvm, main_jvm in enumerate(JVMS):
            comparisons = []
            for id_other, other_jvm in enumerate(JVMS):
                main_data = dict(dict(dict(data.get(bm)).get(main_jvm)).get(s))
                other_data = dict(dict(dict(data.get(bm)).get(other_jvm)).get(s))
                res = compare_performance(main_data, other_data)
                comparisons.append(res)
                total[id_jvm][id_other] += res
                cl = main_data.get("confidence_level")
            m.append(comparisons)
        create_heatmap(bm, s, m, cl)
    create_heatmap("TOTAL", s, total, cl)
