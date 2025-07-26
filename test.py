import time

count = 0.0

start = time.time()

for i in range(10_000_000):
    if i % 2 == 0:
        count += 1
    else:
        count -= 1

end = time.time()

print(f"Count: {count}")
print(f"Elapsed time: {(end - start)*1000:.2f} ms")
print(count)