#!/usr/bin/perl

# In Excel: save as "Windows-kommagetrennt (.csv)
# iconv -f ISO-8859-1 -t UTF-8 App_Localization.csv > App_Localization2.csv

use strict;

my $VALUES = "../app/src/main/res/values";


open IN, "< App_Localization2.csv";

# read titles
my %trans;
my @langs = split /;/, <IN>;
for my $lng (@langs)
{
  $trans{$lng} = ();
}

# skip lines for other apps
my $line;
do
{
  $line = <IN>;
  chomp $line;
} while ($line !~ /^Profile Editor;;;;/);


my $idctr = 0;
while ($line = <IN>)
{
  chomp $line;
  last if ($line =~ /;;;;/);

  my @cols = split /;/, $line;
  for my $c (0..$#langs)
  {
    my $lng = $langs[$c];
#   print "$idctr - $c - $cols[$c]\n";
    $trans{$lng}->{$idctr} = $cols[$c];
  }

  $idctr++;
}
close IN;

sub pickId($)
{
  my $orig = lc(shift());
  for my $k (keys %{$trans{'English'}})
  {
    my $tv = lc($trans{'English'}->{$k});
#   print "COMPARE $k - '$orig' eq '$tv'\n";
    return $k if ($orig eq $tv);
  }
  return undef;
}

my $tlang = { 'fr' => 'French', 'nl' => 'Dutch', 'de' => 'German', 'se' => 'Swedish' };

for my $lk (keys %{$tlang})
{
  open OUT, "> ${VALUES}-$lk/strings.xml";
  close OUT;
}

sub appendAll($)
{
  my $line = shift;
  for my $lk (keys %{$tlang})
  {
    open OUT, ">> ${VALUES}-$lk/strings.xml" || die;
    print OUT "$line\n";
    close OUT;
  }
}

open IN, "< $VALUES/strings.xml";
while ($line = <IN>)
{
  chomp $line;
  
  if ($line =~ /<string name="(\w+)">([^<]+)<\/string>/)
  {
    my $key = $1;
    my $val = $2;
    my $id = &pickId($val);
    if ($id == undef)
    {
      print "no value $key = \"$val\"\n";
      &appendAll($line);
    }
    else
    {
      for my $lk (keys %{$tlang})
      {
#        print "$lk " . $trans{$tlang->{$lk}}->{$id} . "\n";

        my $tr = $trans{$tlang->{$lk}}->{$id};
        if ($tr eq "")
        {
          my $lng = $tlang->{$lk};
          print "no $lng translation for $key = \"$val\"\n";
          $tr = $val;
        }
        open OUT, ">> ${VALUES}-$lk/strings.xml" || die;
        print OUT "    <string name=\"$key\">$tr</string>\n";
        close OUT;
      }
    }
  }
  elsif ($line =~ /<!-- \w+ -->/)
  {
    for my $lk (keys %{$tlang})
    {
      my $lng = $tlang->{$lk};
      open OUT, ">> ${VALUES}-$lk/strings.xml" || die;
      print OUT "    <!-- $lng -->\n";
      close OUT;
    }
  }
  else
  {
    &appendAll($line);
  }
}

close IN;

