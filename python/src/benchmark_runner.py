import subprocess
import os
from datetime import datetime


class Command:
    def __init__(self, name, path, arguments):
        self.name = name
        self.path = path
        self.arguments = arguments


WORKING_DIRECTORY = '/home/knickus/graalvm_performance'
DACAPO_PATH = '/home/knickus/java/dacapo/dacapo-9.12-MR1-bach/dacapo-9.12-MR1-bach.jar'
DEFAULT_DACAPO_ARGS = ['--verbose', '--converge']
DEFAULT_JVM_ARGS = ['-showversion', '-jar']
TEST_ITERATIONS = 50

benchmarks = [Command('avrora',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['avrora']),

              Command('fop',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['fop']),

              Command('h2',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['h2']),

              Command('jython',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['jython']),

              Command('luindex',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['luindex']),

              Command('lusearch-fix',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['lusearch-fix']),

              Command('pmd',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['pmd']),

              Command('sunflow',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['sunflow']),

              Command('tradebeans',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['tradebeans']),

              Command('xalan',
                      DACAPO_PATH,
                      DEFAULT_DACAPO_ARGS + ['xalan'])]

jvms = [Command('GraalVM CE 8',
                '/home/knickus/java/graalvm/graalvm-ce-java8-20.0.0/bin/java',
                DEFAULT_JVM_ARGS),

        Command('GraalVM EE 8',
                '/home/knickus/java/graalvm/graalvm-ee-java8-20.0.0/bin/java',
                DEFAULT_JVM_ARGS),

        Command('GraalVM CE 11',
                '/home/knickus/java/graalvm/graalvm-ce-java11-20.0.0/bin/java',
                DEFAULT_JVM_ARGS),

        Command('GraalVM EE 11',
                '/home/knickus/java/graalvm/graalvm-ee-java11-20.0.0/bin/java',
                DEFAULT_JVM_ARGS),

        Command('OpenJDK 8',
                '/home/knickus/java/openjdk/java-se-8u41-ri/bin/java',
                DEFAULT_JVM_ARGS),

        Command('OpenJDK 11',
                '/home/knickus/java/openjdk/openjdk-11+28/bin/java',
                DEFAULT_JVM_ARGS)]

os.chdir(WORKING_DIRECTORY)

for benchmark in benchmarks:
    for jvm in jvms:
        now = datetime.now().strftime('%Y-%m-%d')
        filename = ' '.join([benchmark.name, jvm.name, now])

        with open('results/' + filename, 'a') as file:
            command = [jvm.path] + jvm.arguments + [benchmark.path] + benchmark.arguments

            file.write('#JVM %s\n' % jvm.name)
            file.write('#BENCHMARK %s\n' % benchmark.name)
            file.write('#COMMAND %s\n' % command)

            print('################')
            print('Running command: %s' % command)
            print('Saving results to: %s' % filename)

            for iteration in range(1, TEST_ITERATIONS + 1):
                print('Iteration: %d' % iteration)

                result = subprocess.run(command,
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.STDOUT,
                                        text=True)

                file.write('#START ITERATION %d\n' % iteration)
                file.write('#CODE %d\n' % result.returncode)
                file.write(result.stdout)
                file.write('#END ITERATION %d\n' % iteration)

                if result.returncode != 0:
                    print(result.stdout)
                    print('An error occured')
                    print('Return code: %s' % result.returncode)
