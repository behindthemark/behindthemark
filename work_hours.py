#!/usr/bin/env python3
"""
work_hours.py - Read a CSV file with log in/log out times and display total worked hours.

CSV format expected:
    Date,Employee,Login,Logout
    2024-01-01,Alice,09:00,17:30
    ...

Usage:
    python3 work_hours.py [csv_file]

    If no file is provided, defaults to 'attendance.csv' in the current directory.
"""

import csv
import sys
from datetime import datetime
from collections import defaultdict


TIME_FORMAT = "%H:%M"


def parse_time(time_str: str) -> datetime:
    """Parse a time string in HH:MM format into a datetime object."""
    return datetime.strptime(time_str.strip(), TIME_FORMAT)


def hours_between(login: str, logout: str) -> float:
    """Return the number of hours worked between login and logout time strings."""
    login_time = parse_time(login)
    logout_time = parse_time(logout)
    delta = logout_time - login_time
    if delta.total_seconds() < 0:
        raise ValueError(f"Logout time '{logout}' is before login time '{login}'")
    return delta.total_seconds() / 3600


def read_attendance(csv_file: str) -> list[dict]:
    """Read attendance records from a CSV file and return a list of row dicts."""
    records = []
    with open(csv_file, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        required_columns = {"Date", "Employee", "Login", "Logout"}
        if not required_columns.issubset(set(reader.fieldnames or [])):
            missing = required_columns - set(reader.fieldnames or [])
            raise ValueError(f"CSV is missing required columns: {missing}")
        for row in reader:
            records.append(row)
    return records


def calculate_hours(records: list[dict]) -> dict[str, float]:
    """
    Calculate total worked hours per employee from a list of attendance records.

    Returns a dict mapping employee name -> total hours worked (float).
    """
    totals: dict[str, float] = defaultdict(float)
    for row in records:
        employee = row["Employee"].strip()
        hours = hours_between(row["Login"], row["Logout"])
        totals[employee] += hours
    return dict(totals)


def display_results(totals: dict[str, float]) -> None:
    """Print a formatted summary of total worked hours per employee."""
    if not totals:
        print("No attendance records found.")
        return

    name_width = max(len(name) for name in totals) + 2
    print(f"\n{'Employee':<{name_width}} {'Total Worked Hours':>20}")
    print("-" * (name_width + 22))
    for employee, hours in sorted(totals.items()):
        total_hours = int(hours)
        total_minutes = round((hours - total_hours) * 60)
        print(f"{employee:<{name_width}} {total_hours:>14}h {total_minutes:02d}m  ({hours:.2f} hrs)")
    print()


def main(csv_file: str = "attendance.csv") -> None:
    """Entry point: read the CSV file and display total worked hours."""
    try:
        records = read_attendance(csv_file)
    except FileNotFoundError:
        print(f"Error: File '{csv_file}' not found.", file=sys.stderr)
        sys.exit(1)
    except ValueError as exc:
        print(f"Error: {exc}", file=sys.stderr)
        sys.exit(1)

    try:
        totals = calculate_hours(records)
    except ValueError as exc:
        print(f"Error in attendance data: {exc}", file=sys.stderr)
        sys.exit(1)

    display_results(totals)


if __name__ == "__main__":
    file_path = sys.argv[1] if len(sys.argv) > 1 else "attendance.csv"
    main(file_path)
