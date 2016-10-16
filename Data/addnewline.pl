#!/usr/bin/perl
use strict;

my @filelist=`ls *digest*`;
foreach my $file(@filelist)
{
    open (DATAFILE, ">>$file");
    print DATAFILE "\n";
    close DATAFILE;
}
